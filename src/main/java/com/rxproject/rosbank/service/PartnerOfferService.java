package com.rxproject.rosbank.service;


import com.rxproject.rosbank.model.PartnerOffer;
import com.rxproject.rosbank.repository.PartnerOfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PartnerOfferService {
    
    private final PartnerOfferRepository partnerOfferRepository;

    @Autowired
    public PartnerOfferService(PartnerOfferRepository partnerOfferRepository) {
        this.partnerOfferRepository = partnerOfferRepository;
    }
    
    public PartnerOffer getById(Long id){
        return partnerOfferRepository.findById(id).orElse(null);
    }
    
    public void save(PartnerOffer partnerOffer){
        partnerOfferRepository.save(partnerOffer);
    }
}
