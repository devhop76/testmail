package org.trivelli.testmail.persistence.jpa;

public enum MessageType {
	ACCETTAZIONE, 
	AVVENUTA_CONSEGNA, 
	ERRORE_CONSEGNA,
	NON_ACCETTAZIONE,
	POSTA_CERTIFICATA,
	PREAVVISO_ERRORE_CONSEGNA;
}
