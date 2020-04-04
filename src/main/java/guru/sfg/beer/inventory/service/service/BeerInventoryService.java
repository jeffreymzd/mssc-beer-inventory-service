package guru.sfg.beer.inventory.service.service;

import guru.sfg.beer.inventory.service.config.JmsConfiguration;
import guru.sfg.beer.inventory.service.domain.BeerInventory;
import guru.sfg.beer.inventory.service.repositories.BeerInventoryRepository;
import guru.sfg.brewery.model.BeerDto;
import guru.sfg.brewery.model.event.NewInventoryEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

/**
 * Created by jeffreymzd on 3/24/20
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BeerInventoryService {

    private final BeerInventoryRepository beerInventoryRepository;

    @JmsListener(destination = JmsConfiguration.NEW_INVENTORY_QUEUE)
    public void listen(NewInventoryEvent newInventoryEvent) {

        BeerDto beerDto = newInventoryEvent.getBeerDto();

        log.info("Restock beer UPC={} quantity={}", beerDto.getUpc(), beerDto.getQuantityOnHand());

        BeerInventory beerInventory = BeerInventory.builder()
                .beerId(beerDto.getId())
                .beerUpc(beerDto.getUpc())
                .quantityOnHand(beerDto.getQuantityOnHand())
                .build();
        beerInventoryRepository.saveAndFlush(beerInventory);
    }
}
