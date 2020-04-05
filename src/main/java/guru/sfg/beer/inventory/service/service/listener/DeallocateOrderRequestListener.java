package guru.sfg.beer.inventory.service.service.listener;

import guru.sfg.beer.inventory.service.config.JmsConfiguration;
import guru.sfg.beer.inventory.service.repositories.BeerInventoryRepository;
import guru.sfg.beer.inventory.service.service.DeallocationService;
import guru.sfg.brewery.model.BeerOrderDto;
import guru.sfg.brewery.model.event.AllocateOrderResult;
import guru.sfg.brewery.model.event.DeallocateOrderRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by jeffreymzd on 4/4/20
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class DeallocateOrderRequestListener {

    private final DeallocationService deallocationService;
    private final BeerInventoryRepository beerInventoryRepository;
    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfiguration.DEALLOCATE_ORDER_QUEUE)
    public void listen(DeallocateOrderRequest deallocateOrderRequest) {
        BeerOrderDto beerOrderDto = deallocateOrderRequest.getBeerOrderDto();

        log.debug("Deallocation request received for order id: {}", beerOrderDto.getId());

        AllocateOrderResult.AllocateOrderResultBuilder builder =
                AllocateOrderResult.builder();
        builder.beerOrderDto(beerOrderDto);
        builder.allocationError(false);

        try {
            Boolean deallocationResult = deallocationService.deallocateOrder(beerOrderDto);
        } catch (Exception e) {
            log.error("Deallocation failed for order id: " + beerOrderDto.getId());
            builder.allocationError(true);
        }

        // this is not required
//        jmsTemplate.convertAndSend(JmsConfiguration.ALLOCATE_ORDER_RESPONSE_QUEUE,
//                builder.build());
    }
}
