package fr.iutvalence.ubpe.core.interfaces;

public interface DataEventForwarder
{
	public void registerDataEventListener(DataEventListener listener);

	public void unregisterDataEventListener(DataEventListener listener);
}
