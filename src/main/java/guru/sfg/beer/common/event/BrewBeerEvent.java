package guru.sfg.beer.common.event;

import guru.sfg.beer.common.model.BeerDto;
import lombok.NoArgsConstructor;

/**
 * Created by jeffreymzd on 3/23/20
 */
@NoArgsConstructor
public class BrewBeerEvent extends BeerEvent {

    private static final long serialVersionUID = -4456108621665223455L;

    public BrewBeerEvent(BeerDto beerDto) {
        super(beerDto);
    }
}
