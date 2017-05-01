package fr.iutvalence.hao.monitor.core.helpers;

import java.io.IOException;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

public class Serial600InputStream extends SerialInputStream
{
	public Serial600InputStream(String serialPortName) throws PortInUseException, NoSuchPortException, UnsupportedCommOperationException, IOException
	{
		super(serialPortName, 600);
	}
}
