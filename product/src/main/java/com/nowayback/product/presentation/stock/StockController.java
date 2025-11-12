package com.nowayback.product.presentation.stock;

import com.nowayback.common.security.annotation.RequireRole;
import com.nowayback.common.security.annotation.UserRole;
import com.nowayback.product.application.stock.StockService;
import com.nowayback.product.application.stock.command.DecreaseStockCommand;
import com.nowayback.product.application.stock.command.IncreaseStockCommand;
import com.nowayback.product.presentation.stock.dto.request.DecreaseStockRequest;
import com.nowayback.product.presentation.stock.dto.request.IncreaseStockRequest;
import com.nowayback.product.presentation.stock.dto.response.StockResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @PatchMapping("/{productId}/increase")
    @RequireRole({UserRole.MASTER, UserRole.HUB_MANAGER})
    public ResponseEntity<StockResponse> increaseStock(
            @PathVariable UUID productId,
            @Valid @RequestBody IncreaseStockRequest request
    ) {
        IncreaseStockCommand command = IncreaseStockCommand.of(
                productId,
                request.amount()
        );

        StockResponse response = StockResponse.from(stockService.increaseStock(command));
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{productId}/decrease")
    @RequireRole({UserRole.MASTER, UserRole.HUB_MANAGER})
    public ResponseEntity<StockResponse> decreaseStock(
            @PathVariable UUID productId,
            @Valid @RequestBody DecreaseStockRequest request
    ) {
        DecreaseStockCommand command = DecreaseStockCommand.of(
                productId,
                request.amount()
        );

        StockResponse response = StockResponse.from(stockService.decreaseStock(command));
        return ResponseEntity.ok(response);
    }
}
