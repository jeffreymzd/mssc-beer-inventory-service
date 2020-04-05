package guru.sfg.beer.inventory.service.service.listener;

import guru.sfg.beer.inventory.service.config.JmsConfiguration;
import guru.sfg.beer.inventory.service.repositories.BeerInventoryRepository;
import guru.sfg.beer.inventory.service.service.AllocationService;
import guru.sfg.brewery.model.BeerOrderDto;
import guru.sfg.brewery.model.event.AllocateOrderRequest;
import guru.sfg.brewery.model.event.AllocateOrderResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by jeffreymzd on 4/2/20
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class AllocateOrderRequestListener {

    private final AllocationService allocationService;
    private final BeerInventoryRepository beerInventoryRepository;
    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfiguration.ALLOCATE_ORDER_QUEUE)
    public void listen(AllocateOrderRequest allocateOrderRequest) {
        BeerOrderDto beerOrderDto = allocateOrderRequest.getBeerOrderDto();

        log.debug("Allocation request received for order id: {}", beerOrderDto.getId());

        AllocateOrderResult.AllocateOrderResultBuilder builder =
                AllocateOrderResult.builder();
        builder.beerOrderDto(beerOrderDto);
        builder.allocationError(false);

        try {
            Boolean allocationResult = allocationService.allocateOrder(beerOrderDto);
            if (allocationResult) {
                builder.pendingInventory(false);
            } else {
                builder.pendingInventory(true);
            }
        } catch (Exception e) {
            log.error("Allocation failed for order id: " + beerOrderDto.getId());
            builder.allocationError(true);
        }

        jmsTemplate.convertAndSend(JmsConfiguration.ALLOCATE_ORDER_RESPONSE_QUEUE,
                builder.build());
    }
}
