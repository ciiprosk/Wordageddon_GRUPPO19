package it.unisa.diem.model.gestione.analisi.stopword;

import it.unisa.diem.model.gestione.analisi.Documento;

public class StopwordENG extends StopwordManager {

    public StopwordENG() {
        super();
    }
    public StopwordENG(Documento documento) {super( documento );}

    @Override protected String[] getArticoli() { return new String[]{
            "the", "a", "an"
    }; }

    @Override protected String[] getPreposizioni() { return new String[]{
            // simple
            "of", "in", "on", "at", "by", "with", "about", "against", "between", "into", "through", "during", "before", "after",
            "above", "below", "to", "from", "up", "down", "off", "over", "under", "around", "among",
            // compound / phrasal
            "according to", "ahead of", "apart from", "as for", "as of", "as per", "as to", "as well as",
            "because of", "close to", "due to", "except for", "far from", "inside of", "instead of", "near to",
            "next to", "on account of", "on top of", "out of", "outside of", "prior to", "pursuant to", "regardless of", "thanks to"
    }; }

    @Override protected String[] getPronomi() { return new String[]{
            // subject
            "i", "you", "he", "she", "it", "we", "they",
            // object
            "me", "you", "him", "her", "it", "us", "them",
            // possessive adjectives
            "my", "your", "his", "her", "its", "our", "their",
            // possessive pronouns
            "mine", "yours", "his", "hers", "ours", "theirs",
            // reflexive
            "myself", "yourself", "himself", "herself", "itself", "ourselves", "yourselves", "themselves"
    }; }

    @Override protected String[] getVerbiAvere() { return new String[]{
            // to have
            "have", "has", "had", "having"
    }; }

    @Override protected String[] getVerbiEssere() { return new String[]{
            // to be
            "be", "am", "is", "are", "was", "were", "being", "been",
    };}

    @Override protected String[] getConnettiviCongiunzioni() { return new String[]{
            "and", "but", "or", "so", "for", "nor", "yet", "although", "though", "even though", "because",
            "since", "unless", "until", "while", "whereas", "whether", "as", "once", "after", "before",
            "however", "therefore", "thus", "moreover", "furthermore", "nonetheless", "nevertheless",
            "in addition", "on the other hand", "in contrast", "otherwise"
    }; }
}