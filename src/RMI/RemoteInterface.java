package RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteInterface extends Remote
{
    public void doRemoteWordCounting( ) throws RemoteException;
}