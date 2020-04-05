package guru.sfg.beer.inventory.service.service;

import guru.sfg.beer.inventory.service.domain.BeerInventory;
import guru.sfg.beer.inventory.service.repositories.BeerInventoryRepository;
import guru.sfg.brewery.model.BeerOrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Created by jeffreymzd on 4/4/20
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class DeallocationServiceImpl implements DeallocationService {

    private final BeerInventoryRepository beerInventoryRepository;

    @Override
    public void deallocateOrder(BeerOrderDto beerOrderDto) {

        log.debug("De-allocating beer order: {}", beerOrderDto.getId());

        beerOrderDto.getBeerOrderLines().forEach(beerOrderLineDto -> {
            BeerInventory beerInventory = beerInventoryRepository.saveAndFlush(BeerInventory.builder()
                    .beerId(beerOrderLineDto.getBeerId())
                    .beerUpc(beerOrderLineDto.getUpc())
                    .quantityOnHand(beerOrderLineDto.getQuantityAllocated())
                    .build());

            log.debug("Save inventory {}: beerId={} upc={} qty={}", beerInventory.getId(),
                    beerOrderLineDto.getBeerId(), beerOrderLineDto.getUpc(), beerOrderLineDto.getQuantityAllocated());
        });

        log.debug("De-allocated beer order: {}", beerOrderDto.getId());
    }
}
