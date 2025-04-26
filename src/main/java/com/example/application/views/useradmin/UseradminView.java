package com.example.application.views.useradmin;

import com.example.application.data.PatientOverview;
import com.example.application.data.User;
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

@PageTitle("User Search")
@Route("useradmin")
@Menu(order = 2, icon = LineAwesomeIconUrl.SEARCHENGIN)
@RolesAllowed("USER")
public class UseradminView extends HorizontalLayout {

    private TextField name;
    private Button search;
    private final Grid<User> grid = new Grid<>(User.class, false);
    private List<User> users;
    private final UserService userService;
    public UseradminView(UserService userService) {
        this.userService = userService;
        name = new TextField("Patient's name");
        search = new Button("Search");

        VerticalLayout verticalLayout  = new VerticalLayout();
        grid.addColumn("name").setAutoWidth(true);
        grid.addColumn("username").setAutoWidth(true);
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(name, search);
        verticalLayout.add(horizontalLayout,grid);

        search.addClickListener(e -> {
            if(name.isEmpty()) {
                grid.setItems(Collections.emptyList());
                Notification notification = new Notification("Please enter Patient's name");
                return;
            }
            users = userService.findByNameStartingWith(name.getValue());
            if (users != null) {
                grid.setItems(users);
            }
        });
        search.addClickShortcut(Key.ENTER);

        setMargin(true);
        setVerticalComponentAlignment(Alignment.END, name, search);

        add(verticalLayout);


    }

}
