package com.example.application.views.crudops;


import com.example.application.data.PatientOverview;
import com.example.application.data.User;
import com.vaadin.flow.component.button.Button;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class FiltersForPatientOverview extends Div implements Specification<PatientOverview> {

    private TextField allergies = new TextField("Allergies");

    private TextField chronicConditions = new TextField("Chronic Conditions");

    private TextField medications = new TextField("Medications");

    private TextField bloodType = new TextField("Blood Type");

    private TextField user = new TextField("Patient's name");

    private Button searchButton = new Button("Search");

    private Button clearButton = new Button("Clear");

    public FiltersForPatientOverview(Runnable onSearch) {
        addClassNames("filters-component");
        searchButton.addClassNames("filters-button-search");
        clearButton.addClassNames("filters-button-clear");
        clearButton.addClickListener(e -> {
            allergies.clear();
            chronicConditions.clear();
            medications.clear();
            bloodType.clear();
            user.clear();
            onSearch.run();
        });

        searchButton.addClickListener(e -> {
            onSearch.run();
        });

        HorizontalLayout layout = new HorizontalLayout();
        layout.add(user,allergies,chronicConditions,medications,bloodType, searchButton, clearButton);
        add(layout);
    }

    @Override
    public Predicate toPredicate(Root<PatientOverview> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicateList = new ArrayList<>();

        if(!allergies.isEmpty()) {
            Predicate allergiesPredicate =  criteriaBuilder.like(criteriaBuilder.lower(root.get("allergies")),
                    allergies.getValue().toLowerCase() + "%");
            predicateList.add(allergiesPredicate);
        }
        if(!chronicConditions.isEmpty()) {
            Predicate chronicConditionsPredicate =  criteriaBuilder.like(criteriaBuilder.lower(root.get("chronicConditions")),
                    chronicConditions.getValue().toLowerCase() + "%");
            predicateList.add(chronicConditionsPredicate);
        }
        if(!medications.isEmpty()) {
            Predicate medicationsPredicate =  criteriaBuilder.like(criteriaBuilder.lower(root.get("medications")),
                    medications.getValue().toLowerCase() + "%");
            predicateList.add(medicationsPredicate);
        }
        if(!bloodType.isEmpty()) {
            Predicate bloodTypePredicate =  criteriaBuilder.like(criteriaBuilder.lower(root.get("bloodType")),
                    bloodType.getValue().toLowerCase() + "%");
            predicateList.add(bloodTypePredicate);
        }

        if(!user.isEmpty()) {
            Join<PatientOverview, User> userJoin = root.join("user");
            Predicate userPredicate = criteriaBuilder.like(criteriaBuilder.lower(userJoin.get("name")), user.getValue().toLowerCase() + "%");
            predicateList.add(userPredicate);
        }
        return criteriaBuilder.and(predicateList.toArray(Predicate[]::new));

    }
}
