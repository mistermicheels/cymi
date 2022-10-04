package com.mistermicheels.cymi.component.group.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.mistermicheels.cymi.common.FieldLengths;
import com.mistermicheels.cymi.common.error.InvalidRequestException;

@Entity
@Table(name = "group_definition")
public class Group implements IGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = FieldLengths.DEFAULT_STRING_LENGTH)
    private String name;

    @OneToMany(mappedBy = "group")
    private Set<GroupMembership> members;

    Group() {
    }

    public Group(String name) {
        if (name.length() > FieldLengths.DEFAULT_STRING_LENGTH) {
            throw new InvalidRequestException("Name should not be longer than "
                    + FieldLengths.DEFAULT_STRING_LENGTH + " characters");
        }

        this.name = name;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

}
