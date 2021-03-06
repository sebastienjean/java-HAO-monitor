package fr.iutvalence.hao.monitor.core.interfaces;

public interface DataEventForwarder
{
	public void registerDataEventListener(DataEventListener listener);

	public void unregisterDataEventListener(DataEventListener listener);

	public void forward(DataEvent event);
}
