package com.github.mellemahp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class PreferenceRanking {
    public List<Preference> preferenceList;
    public Map<Person, Preference> preferenceMap;

    public PreferenceRanking() {
        this.preferenceList = new ArrayList<>();
        this.preferenceMap = new HashMap<>(); 
    }

    public void add(Preference preference) {
        // Add to map
        this.preferenceMap.put(preference.getPerson(), preference);

        // Add to sorted list
        this.preferenceList.add(preference);
        this.preferenceList.sort((p1, p2) -> p2.getPreferenceScore(0).compareTo(p2.getPreferenceScore(0)))
    }

    public double getPreferenceScore(Person person) { 
        return this.preferenceMap.get(person).getPreferenceScore(0);
    }

    public Person getPerson(int index) { 
        return this.preferenceList.get(index).getPerson();
    }
    
    public int getPreferenceIndexOfPerson(Person person) {
        // NOTE: There is no equals implementation for Person, so it will use the memory address when
        // using equals. We will need to implement equals for the Person class.

        return IntStream.range(0, this.preferenceList.size())
            .filter(i -> this.preferenceList.get(i).getPerson().equals(person))
            .findFirst()
            .getAsInt();
    }

}