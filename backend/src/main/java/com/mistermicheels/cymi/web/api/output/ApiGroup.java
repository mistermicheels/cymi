package com.mistermicheels.cymi.web.api.output;

import com.mistermicheels.cymi.component.group.entity.Group;

public abstract class ApiGroup {

    private final Long id;
    private final String name;

    public ApiGroup(Group group) {
        this.id = group.getId();
        this.name = group.getName();
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

}
