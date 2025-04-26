package com.example.application.views.crudops;

import com.example.application.data.PatientOverview;
import com.example.application.data.User;
import com.example.application.data.UserRepository;
import com.example.application.services.PatientOverviewService;
import com.example.application.services.UserService;
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
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import jakarta.annotation.security.RolesAllowed;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@PageTitle("Patient's overview search")
@Route("patientoverviewSearch")
@Menu(order = 3, icon = LineAwesomeIconUrl.SEARCH_SOLID)
@RolesAllowed("ADMIN")
public class PatientOverviewSearch extends HorizontalLayout {

    private final UserRepository userRepository;
    private TextField name;
    private Button search;
    private final Grid<PatientOverview> grid = new Grid<>(PatientOverview.class, false);
    private List<PatientOverview> patientOverviews;
    private final PatientOverviewService patientOverviewService;
    public PatientOverviewSearch(PatientOverviewService patientOverviewService, UserRepository userRepository) {
        this.patientOverviewService = patientOverviewService;
        name = new TextField("Patient's name");
        search = new Button("Search");

        VerticalLayout verticalLayout  = new VerticalLayout();
        grid.addColumn("allergies").setAutoWidth(true);
        grid.addColumn("chronicConditions").setAutoWidth(true);
        grid.addColumn("medications").setAutoWidth(true);
        grid.addColumn("bloodType").setAutoWidth(true);
        grid.addColumn(patientOverview -> patientOverview.getUser() != null ? patientOverview.getUser().getName() : null).setAutoWidth(true).setHeader("user");
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(name, search);
        verticalLayout.add(horizontalLayout,grid);

        search.addClickListener(e -> {
            if(name.isEmpty()) {
                grid.setItems(Collections.emptyList());
                Notification notification = new Notification("Please enter Patient's name");
                return;
            }
            List<PatientOverview> patientOverviews = patientOverviewService.findByNameStartingWith(name.getValue());
            if (patientOverviews != null) {
                for (PatientOverview patientOverview : patientOverviews) {
                    System.out.println(patientOverview.getAllergies());
                }
                grid.setItems(patientOverviews);
            }
            else {
                Notification.show("No patient overviews found for usernames starting with: " + name, 3000, Notification.Position.MIDDLE);
                grid.setItems(Collections.emptyList()); // Tyhjennä Grid
            }
        });
        search.addClickShortcut(Key.ENTER);

        setMargin(true);
        setVerticalComponentAlignment(Alignment.END, name, search);

        add(verticalLayout);
        this.userRepository = userRepository;
    }

}
