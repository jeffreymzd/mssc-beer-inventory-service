package guru.sfg.beer.inventory.service.service;

import guru.sfg.brewery.model.BeerOrderDto;

/**
 * Created by jeffreymzd on 4/4/20
 */
public interface DeallocationService {
    void deallocateOrder(BeerOrderDto beerOrderDto);
}
