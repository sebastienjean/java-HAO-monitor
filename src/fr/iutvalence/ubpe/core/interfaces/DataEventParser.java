package fr.iutvalence.ubpe.core.interfaces;

import fr.iutvalence.ubpe.core.exceptions.ParsingException;

public interface DataEventParser
{	
	public DataEvent parse() throws ParsingException;
}
