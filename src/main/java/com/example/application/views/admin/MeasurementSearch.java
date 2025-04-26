package com.example.application.views.admin;
import com.example.application.data.Measurement;
import com.example.application.data.UserRepository;
import com.example.application.services.MeasurementService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.util.Collections;
import java.util.List;

@PageTitle("Measurements search")
@Route("measurementSearch")
@Menu(order = 5, icon = LineAwesomeIconUrl.SEARCH_SOLID)
@RolesAllowed("ADMIN")
public class MeasurementSearch extends HorizontalLayout {

    private final UserRepository userRepository;
    private TextField measurement;
    private Button search;
    private final Grid<Measurement> grid = new Grid<>(Measurement.class, false);
    private List<Measurement> measurements;
    private final MeasurementService measurementService;
    public MeasurementSearch(MeasurementService measurementService, UserRepository userRepository) {
        this.measurementService = measurementService;
        measurement = new TextField("Measurement's name");
        search = new Button("Search");

        VerticalLayout verticalLayout  = new VerticalLayout();
        grid.addColumn("measurementName").setAutoWidth(true);
        grid.addColumn("measurementValue").setAutoWidth(true);
        grid.addColumn("measurementUnit").setAutoWidth(true);
        grid.addColumn("date").setAutoWidth(true);
        grid.addColumn("description").setAutoWidth(true);
        grid.addColumn(measurement -> measurement.getUser() != null ? measurement.getUser().getName() : null).setAutoWidth(true).setHeader("user");
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(measurement, search);
        verticalLayout.add(horizontalLayout,grid);

        search.addClickListener(e -> {
            if(measurement.isEmpty()) {
                grid.setItems(Collections.emptyList());
                Notification notification = new Notification("Please enter Measurement's name");
                return;
            }
            List<Measurement> measurements = measurementService.getMeasurements(measurement.getValue());
            if (measurements != null) {
                grid.setItems(measurements);
            }
            else {
                Notification.show("No measurements found for measurement name starting with: " + measurement, 3000, Notification.Position.MIDDLE);
                grid.setItems(Collections.emptyList());
            }
        });
        search.addClickShortcut(Key.ENTER);

        setMargin(true);
        setVerticalComponentAlignment(Alignment.END, measurement, search);

        add(verticalLayout);
        this.userRepository = userRepository;
    }

}
