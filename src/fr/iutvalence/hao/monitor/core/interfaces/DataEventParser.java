package fr.iutvalence.hao.monitor.core.interfaces;

import fr.iutvalence.hao.monitor.core.exceptions.ParsingException;

public interface DataEventParser
{
	public DataEvent parse(byte[] eventData) throws ParsingException;
}
