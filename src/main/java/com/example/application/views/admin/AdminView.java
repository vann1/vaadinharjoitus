package com.example.application.views.admin;

import com.example.application.data.Measurement;
import com.example.application.data.SamplePerson;
import com.example.application.data.User;
import com.example.application.services.MeasurementService;
import com.example.application.services.SamplePersonService;
import com.example.application.services.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
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
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import jakarta.annotation.security.RolesAllowed;
import java.util.Optional;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Patient's measurements")
@Route("admin/:measurementID?/:action?(edit)")
@Menu(order = 2, icon = LineAwesomeIconUrl.COLUMNS_SOLID)
@RolesAllowed("ADMIN")
@Uses(Icon.class)
public class AdminView extends Div implements BeforeEnterObserver {

    //private final String MEASUREMENT_ID = "measurementID";
    //private final String MEASUREMENT_EDIT_ROUTE_TEMPLATE = "admin/%s/edit";
    private Measurement measurement;
    private final Grid<Measurement> grid = new Grid<>(Measurement.class, false);
    private final BeanValidationBinder<Measurement> binder;

    private final MeasurementService measurementService;

    private Filters filters;

    public AdminView(MeasurementService measurementService) {
        this.measurementService = measurementService;
        binder = new BeanValidationBinder<>(Measurement.class);
        addClassNames("admin-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        //createEditorLayout(splitLayout);

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
                //UI.getCurrent().navigate(String.format(MEASUREMENT_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));

            } else {
                clearForm();
                UI.getCurrent().navigate(AdminView.class);
            }
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
    /*    Optional<Long> measurementId = event.getRouteParameters().get(MEASUREMENT_ID).map(Long::parseLong);
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
        } */
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
