package fr.iutvalence.hao.monitor.core.helpers;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;

public class SerialInputStream extends InputStream
{
	private InputStream in;

	public SerialInputStream(String serialPortName, int baudRate) throws PortInUseException, NoSuchPortException, UnsupportedCommOperationException, IOException
	{
		SerialPort port = null;

		port = (SerialPort) CommPortIdentifier.getPortIdentifier(serialPortName).open("SerialInputStream", 2000);
		port.setSerialPortParams(baudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		port.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
		// 10 seconds
		port.enableReceiveTimeout(10000);

		this.in = port.getInputStream();

	}

	@Override
	public int read() throws IOException
	{
		return this.in.read();
	}

	@Override
	public int available() throws IOException
	{
		return this.in.available();
	}

	@Override
	public void close() throws IOException
	{
		this.in.close();
	}
}
