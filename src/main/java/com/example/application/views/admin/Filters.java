package com.example.application.views.admin;

import com.example.application.data.Measurement;
import com.example.application.data.User;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;

public class Filters extends Div implements Specification<Measurement> {

    private TextField measurementName = new TextField("Measurement Name");

    private DatePicker dateStart = new DatePicker("Start Date");

    private DatePicker dateEnd = new DatePicker("End Date");

    private TextField user = new TextField("Patient name");

    private Button searchButton = new Button("");

    private Button clearButton = new Button("");

    public Filters(Runnable onSearch) {
        addClassNames("filters-component");
        searchButton.addClassNames("filters-button-search");
        clearButton.addClassNames("filters-button-clear");
        clearButton.addClickListener(e -> {
            measurementName.clear();
            dateStart.clear();
            dateEnd.clear();
            user.clear();
            onSearch.run();
        });

        searchButton.addClickListener(e -> {
            onSearch.run();
        });

        HorizontalLayout layout = new HorizontalLayout();
        layout.add(measurementName,dateStart,dateEnd,user, searchButton, clearButton);
        add(layout);
    }

    @Override
    public Predicate toPredicate(Root<Measurement> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicateList = new ArrayList<>();

        if(!measurementName.isEmpty()) {
            Predicate measurementNamePredicate =  criteriaBuilder.like(criteriaBuilder.lower(root.get("measurementName")),
                    measurementName.getValue().toLowerCase() + "%");
            predicateList.add(measurementNamePredicate);
        }
        if(!dateStart.isEmpty()){
            Predicate dateStartPredicate = criteriaBuilder.greaterThanOrEqualTo(
                    root.get("date"),
                    criteriaBuilder.literal(dateStart.getValue()));
            predicateList.add(dateStartPredicate);
        }
        if(!dateEnd.isEmpty()){
            Predicate dateEndPredicate = criteriaBuilder.greaterThanOrEqualTo(
                    criteriaBuilder.literal(dateEnd.getValue())
                    ,root.get("date"));
            predicateList.add(dateEndPredicate);
        }
        if(!user.isEmpty()) {
            Join<Measurement, User> userJoin = root.join("user");
            Predicate userPredicate = criteriaBuilder.like(criteriaBuilder.lower(userJoin.get("name")), user.getValue().toLowerCase() + "%");
            predicateList.add(userPredicate);
        }
        return criteriaBuilder.and(predicateList.toArray(Predicate[]::new));

    }
}
