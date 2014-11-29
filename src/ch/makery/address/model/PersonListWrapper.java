/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.makery.address.model;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe auxiliar para envolver uma lista de pessoas. Esta é usada para salvar a
 * lista de pessoas em XML.
 * @author Emisvaldo Silva
 */
@XmlRootElement(name = "persons")
public class PersonListWrapper {
    
    private List<Person> persons;
    
    @XmlElement(name = "persons")
    public List<Person> getPersons(){
        return persons;
    }
    
    public void setPersons(List<Person> persons){
        this.persons = persons;
    }
}
