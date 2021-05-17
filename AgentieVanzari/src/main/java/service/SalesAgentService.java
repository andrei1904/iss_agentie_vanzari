package service;

import model.SalesAgent;
import repository.SalesAgentRepository;

public class SalesAgentService {
    private final SalesAgentRepository salesAgentRepository;

    public SalesAgentService(SalesAgentRepository salesAgentRepository) {
        this.salesAgentRepository = salesAgentRepository;
    }

    public SalesAgent login(String username, String password) {
        SalesAgent res = salesAgentRepository.findOne(username);

        if (res != null && res.getPassword().equals(password)) {
            return res;
        }

        return null;
    }
}
