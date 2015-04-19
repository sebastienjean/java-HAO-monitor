package fr.iutvalence.hao.monitor.core.interfaces;

import fr.iutvalence.hao.monitor.core.exceptions.ParsingException;

public interface DataEventParserForwarder extends DataEventParser, DataEventForwarder
{
	public void parseAndForward(byte[] eventData) throws ParsingException;
}
