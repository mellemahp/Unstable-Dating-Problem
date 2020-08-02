package com.github.mellemahp.person;

import com.github.mellemahp.events.Event;
import com.github.mellemahp.events.EventBus;

import org.apache.commons.math3.distribution.RealDistribution;

public class Suitor extends Person {
    public Suitor(double objectiveAttractivenessScore, RealDistribution preferenceDistribution) {
        super(objectiveAttractivenessScore, preferenceDistribution);
    }

    public void propose(EventBus bus) {
        if (this.currentPartner == null) {
            Suitee currentSuitee = (Suitee) this.preferenceRanking.getPerson(this.preferenceIndex);
            ProposalAnswer answer = currentSuitee.reviewProposal(this);
            
            switch (answer) {
                case ACCEPT:
                    bus.put_event(Event.NEW_PARTNER);
                    this.currentPartner = currentSuitee;
                    break;
                case REJECT:
                    this.preferenceIndex++;
                    break;
            }
        }        
    }
}