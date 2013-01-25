package fr.iutvalence.ubpe.core.interfaces;

import fr.iutvalence.ubpe.core.exceptions.ParsingException;

public interface DataEventParserForwarder extends DataEventParser, DataEventForwarder
{
	public void parseAndForward(byte[] eventData) throws ParsingException;
}
