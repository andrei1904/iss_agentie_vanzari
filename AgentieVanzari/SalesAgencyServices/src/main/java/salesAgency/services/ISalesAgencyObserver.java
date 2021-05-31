package salesAgency.services;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ISalesAgencyObserver extends Remote {
    public void orderChanged() throws SalesAgencyException, RemoteException;
}

