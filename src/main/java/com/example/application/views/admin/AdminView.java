package com.example.application.views.admin;

import com.example.application.data.Measurement;
import com.example.application.data.User;
import com.example.application.services.MeasurementService;
import com.example.application.services.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
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
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import jakarta.annotation.security.RolesAllowed;
import java.util.Optional;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Patient's measurements add/edit")
@Route("admin/:measurementID?/:action?(edit)")
@Menu(order = 6, icon = LineAwesomeIconUrl.EDIT)
@RolesAllowed("ADMIN")
@Uses(Icon.class)
public class AdminView extends Div implements BeforeEnterObserver {

    private final String MEASUREMENT_ID = "measurementID";
    private final String MEASUREMENT_EDIT_ROUTE_TEMPLATE = "admin/%s/edit";
    private Measurement measurement;
    private final Grid<Measurement> grid = new Grid<>(Measurement.class, false);
    private final BeanValidationBinder<Measurement> binder;

    private final MeasurementService measurementService;

    private TextField measurementName;
    private TextField measurementValue;
    private TextField measurementUnit;
    private TextField description;
    private DatePicker date;
    private ComboBox<User> user;

    private final UserService userService;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");
    private final Button delete = new Button("Delete");


    private Filters filters;

    public AdminView(MeasurementService measurementService,
                     UserService userService) {
        this.measurementService = measurementService;
        this.userService = userService;

        addClassNames("admin-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("measurementName").setAutoWidth(true);
        grid.addColumn("measurementValue").setAutoWidth(true);
        grid.addColumn("measurementUnit").setAutoWidth(true);
        grid.addColumn("date").setAutoWidth(true);
        grid.addColumn("description").setAutoWidth(true);
        grid.addColumn(measurement -> measurement.getUser() != null ? measurement.getUser().getName() : "").setHeader("Patient").setAutoWidth(true);



        grid.setItems(query -> measurementService.list(VaadinSpringDataHelpers.toSpringPageRequest(query),filters).stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(MEASUREMENT_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));

            } else {
                clearForm();
                UI.getCurrent().navigate(AdminView.class);
            }
        });

        // Add user information to combobox

        user.setItems(this.userService.getUsers());
        user.setItemLabelGenerator(User::getName);


        // Configure Form
        binder = new BeanValidationBinder<>(Measurement.class);

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.measurement == null) {
                    this.measurement = new Measurement();
                }

                binder.writeBean(this.measurement);

                if (this.measurement.getMeasurementName().isEmpty()
                        || this.measurement.getMeasurementUnit().isEmpty()
                        || this.measurement.getMeasurementValue().isEmpty()
                        || this.measurement.getDate() == null
                        || this.measurement.getUser() == null
                        || this.measurement.getDescription().isEmpty()) {
                    Notification.show("Täytä kaikki kentät.");
                    return;
                }
                measurementService.save(this.measurement);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(AdminView.class);
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
            if(this.measurement == null) {
                return;
            }
            measurementService.delete(this.measurement.getId());
            refreshGrid();
            clearForm();
            Notification.show("Measurment deleted");
            UI.getCurrent().navigate(AdminView.class);
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> measurementId = event.getRouteParameters().get(MEASUREMENT_ID).map(Long::parseLong);
        if (measurementId.isPresent()) {
            Optional<Measurement> measurementFromBackend = measurementService.get(measurementId.get());
            if (measurementFromBackend.isPresent()) {
                populateForm(measurementFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested measurement was not found, ID = %s", measurementId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(AdminView.class);
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
        measurementName = new TextField("Measurement name");
        measurementValue = new TextField("Measurement value");
        measurementUnit = new TextField("Measurement unit");
        description = new TextField("Description");
        date = new DatePicker("Date");
        user = new ComboBox<>("Patient name");

        formLayout.add(measurementName, measurementValue, measurementUnit, description,date, user);

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
        filters = new Filters(this::refreshGrid);
        wrapper.setClassName("grid-wrapper");
        wrapper.setHeightFull();
        splitLayout.addToPrimary(verticalLayout);
        verticalLayout.add(filters,wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Measurement value) {
        this.measurement = value;
        binder.readBean(this.measurement);

    }
}
