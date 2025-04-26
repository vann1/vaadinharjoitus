package com.example.application.views.mainview;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.awt.*;

@PageTitle("Main view")
@Route("")
@Menu(order = 1, icon = LineAwesomeIconUrl.DRAGON_SOLID)
@AnonymousAllowed
public class MainviewView extends VerticalLayout {

    public MainviewView() {
        addClassNames("main-view");
        setSpacing(false);

        Image img = new Image("images/Untitled.png", "placeholder plant");
        img.setWidth("700px");
        add(img);

        H2 header = new H2("Please sign in as A user/user to see useradmin view or admin/admin to see admin view and the rest");
        header.addClassNames("main-view-header");
        header.getStyle().set("font-size", "20px");
        add(header);

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

}
