package org.hibernate.test;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "person")
public class Person {
    public static final String NICKNAME = "nickname";
    @Id
    private int id;
    private String firstName;
    private String lastName;
    @Convert(converter = HashMapConverter.class)
    private Map<String, Object> attributes = new HashMap<>();


    public Person() {
    }

    public Person(int id, String firstName, String lastName, String nickName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.attributes.put(NICKNAME, nickName);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
}
