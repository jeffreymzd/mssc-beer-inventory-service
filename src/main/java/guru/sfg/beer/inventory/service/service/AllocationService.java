package guru.sfg.beer.inventory.service.service;

import guru.sfg.brewery.model.BeerOrderDto;

/**
 * Created by jeffreymzd on 4/1/20
 */
public interface AllocationService {

    Boolean allocateOrder(BeerOrderDto beerOrderDto);
}
