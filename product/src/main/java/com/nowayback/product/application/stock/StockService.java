package com.nowayback.product.application.stock;

import com.nowayback.product.application.stock.command.DecreaseStockCommand;
import com.nowayback.product.application.stock.command.IncreaseStockCommand;
import com.nowayback.product.application.stock.dto.StockResult;
import com.nowayback.product.application.stock.exception.StockApplicationErrorCode;
import com.nowayback.product.application.stock.exception.StockApplicationException;
import com.nowayback.product.domain.stock.entity.Stock;
import com.nowayback.product.domain.stock.repository.StockRepository;
import com.nowayback.product.domain.stock.vo.ProductId;
import com.nowayback.product.domain.stock.vo.Quantity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    public StockResult createStock(UUID productId) {
        validateDuplicateProductId(productId);

        Stock stock = Stock.create(ProductId.of(productId));
        Stock savedStock = stockRepository.save(stock);
        return StockResult.from(savedStock);
    }

    public StockResult getStock(UUID productId) {
        Stock stock = getStockByProductId(ProductId.of(productId));
        return StockResult.from(stock);
    }

    @Transactional
    public StockResult increaseStock(IncreaseStockCommand command) {
        Stock stock = getStockByProductId(command.productId());
        Quantity quantity = stock.getQuantity();

        Quantity increasedQuantity = quantity.add(command.amount());
        stock.updateQuantity(increasedQuantity);

        return StockResult.from(stock);
    }

    @Transactional
    public StockResult decreaseStock(DecreaseStockCommand command) {
        Stock stock = getStockByProductId(command.productId());
        Quantity quantity = stock.getQuantity();

        Quantity decreasedQuantity = quantity.subtract(command.amount());
        stock.updateQuantity(decreasedQuantity);

        return StockResult.from(stock);
    }

    public void deleteStock(UUID actorId, UUID productId) {
        Stock stock = getStockByProductId(ProductId.of(productId));
        stock.delete(actorId);
        stockRepository.save(stock);
    }

    private Stock getStockByProductId(ProductId productId) {
        return stockRepository.findByProductId(productId)
                .orElseThrow(() -> new StockApplicationException(StockApplicationErrorCode.NOT_FOUND_STOCK));
    }

    private void validateDuplicateProductId(UUID productId) {
        if (stockRepository.existsByProductId(ProductId.of(productId))) {
            throw new StockApplicationException(StockApplicationErrorCode.DUPLICATE_PRODUCT_ID);
        }
    }
}
