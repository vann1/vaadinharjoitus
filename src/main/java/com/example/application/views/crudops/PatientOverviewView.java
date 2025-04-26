package com.example.application.views.crudops;

import com.example.application.data.PatientOverview;
import com.example.application.data.PatientOverviewRepository;
import com.example.application.data.User;
import com.example.application.services.PatientOverviewService;
import com.example.application.services.UserService;
import com.example.application.views.admin.Filters;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;

import java.util.Optional;


import jakarta.annotation.security.RolesAllowed;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Patient's overview add/edit")
@Route("patientoverview/:patientOverviewId?/:action?(edit)")
@Menu(order = 4, icon = LineAwesomeIconUrl.EDIT)
@Uses(Icon.class)
@RolesAllowed("ADMIN")
public class PatientOverviewView extends Div implements BeforeEnterObserver {

    private final String PATIENTOVERVIEW_ID = "patientOverviewId";
    private final String PATIENTOVERVIEW_EDIT_ROUTE_TEMPLATE = "patientoverview/%s/edit";

    private final Grid<PatientOverview> grid = new Grid<>(PatientOverview.class, false);
    private final PatientOverviewRepository patientOverviewRepository;

    private TextField allergies;
    private TextField chronicConditions;
    private TextField medications;
    private TextField bloodType;
    private ComboBox<User> user;



    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");
    private final Button delete = new Button("Delete");

    private final BeanValidationBinder<PatientOverview> binder;

    private PatientOverview patientOverview;

    private final PatientOverviewService patientOverviewService;

    private FiltersForPatientOverview filtersForPatientOverview;

    private final UserService userService;

    public PatientOverviewView(PatientOverviewService patientOverviewService,
                            UserService userService,
                               PatientOverviewRepository patientOverviewRepository) {
        this.patientOverviewService = patientOverviewService;
        this.userService = userService;


        addClassNames("admin-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("allergies").setAutoWidth(true);
        grid.addColumn("chronicConditions").setAutoWidth(true);
        grid.addColumn("medications").setAutoWidth(true);
        grid.addColumn("bloodType").setAutoWidth(true);

        grid.addColumn(patientOverview -> patientOverview.getUser() != null ? patientOverview.getUser().getName() : "null")
                .setAutoWidth(true)
                .setHeader("Patient name");


        grid.setItems(query -> patientOverviewService.list(VaadinSpringDataHelpers.toSpringPageRequest(query),filtersForPatientOverview).stream());

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(PATIENTOVERVIEW_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(PatientOverviewView.class);
            }
        });

        // Add user information to combobox

        user.setItems(this.userService.getUsers());
        user.setItemLabelGenerator(User::getName);


        // Configure Form
        binder = new BeanValidationBinder<>(PatientOverview.class);

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.patientOverview == null) {
                    this.patientOverview = new PatientOverview();
                }

                binder.writeBean(this.patientOverview);
                if(this.patientOverview.getUser() != null) {
                    Optional<PatientOverview> existing = patientOverviewService.getPatientOverviewByUser(patientOverview.getUser());
                    if(existing.isPresent() && !existing.get().getId().equals(patientOverview.getId())) {
                        Notification.show("Potilaalla voi olla vain yksi yleiskatsaus ja yksi yleiskatsaus potilasta kohden!");
                        return;
                    }
                }
                if (this.patientOverview.getAllergies().isEmpty()
                        || this.patientOverview.getUser() == null
                        || this.patientOverview.getMedications().isEmpty()
                        || this.patientOverview.getBloodType().isEmpty()
                || this.patientOverview.getChronicConditions().isEmpty()) {
                    Notification.show("Täytä kaikki kentät.");
                    return;
                }
                patientOverviewService.save(this.patientOverview);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(PatientOverviewView.class);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error updating the data. Somebody else has updated the record while you were making changes.");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (ValidationException validationException) {
                Notification.show("Failed to update the data. Check again that all values are valid");
            }
        });

        delete.addClickListener(e -> {
            if(this.patientOverview == null) {
                return;
            }
            patientOverviewService.delete(this.patientOverview.getId());
            refreshGrid();
            clearForm();
            Notification.show("Patient overview deleted");
            UI.getCurrent().navigate(PatientOverviewView.class);
        });
        this.patientOverviewRepository = patientOverviewRepository;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> patientOverviewId = event.getRouteParameters().get(PATIENTOVERVIEW_ID).map(Long::parseLong);
        if (patientOverviewId.isPresent()) {
            Optional<PatientOverview> patientOverviewFromBackend = patientOverviewService.get(patientOverviewId.get());
            if (patientOverviewFromBackend.isPresent()) {
                populateForm(patientOverviewFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested patient overview was not found, ID = %s", patientOverviewId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(PatientOverviewView.class);
            }
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        allergies = new TextField("Allergies");
        chronicConditions = new TextField("Chronic conditions");
        medications = new TextField("Medications");
        bloodType = new TextField("Blood Type");
        user = new ComboBox<>("Patient name");


        formLayout.add(allergies, chronicConditions, medications, bloodType, user);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        buttonLayout.add(save, cancel, delete);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        VerticalLayout verticalLayout = new VerticalLayout();
        Div wrapper = new Div();
        filtersForPatientOverview = new FiltersForPatientOverview(this::refreshGrid);
        wrapper.setClassName("grid-wrapper");
        wrapper.setHeightFull();
        splitLayout.addToPrimary(verticalLayout);
        verticalLayout.add(filtersForPatientOverview,wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(PatientOverview value) {
        this.patientOverview = value;
        binder.readBean(this.patientOverview);

    }
}
