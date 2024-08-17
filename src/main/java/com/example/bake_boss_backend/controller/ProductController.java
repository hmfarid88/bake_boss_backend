package com.example.bake_boss_backend.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bake_boss_backend.dto.MadeItemDTO;
import com.example.bake_boss_backend.dto.MaterialGroupedDto;
import com.example.bake_boss_backend.entity.CategoryName;
import com.example.bake_boss_backend.entity.ItemMake;
import com.example.bake_boss_backend.entity.MaterialName;
import com.example.bake_boss_backend.entity.MaterialsStock;
import com.example.bake_boss_backend.entity.ProductRate;
import com.example.bake_boss_backend.entity.ProductStock;
import com.example.bake_boss_backend.entity.SalesStock;
import com.example.bake_boss_backend.entity.SupplierName;
import com.example.bake_boss_backend.repository.CategoryNameRepository;
import com.example.bake_boss_backend.repository.ItemMakeRepository;
import com.example.bake_boss_backend.repository.MaterialsNameRepository;
import com.example.bake_boss_backend.repository.MaterialsRepository;
import com.example.bake_boss_backend.repository.ProductStockrepository;
import com.example.bake_boss_backend.repository.SalesStockRepository;
import com.example.bake_boss_backend.repository.SupplierNameRepository;
import com.example.bake_boss_backend.service.ItemMakeService;
import com.example.bake_boss_backend.service.ProductStockService;
import com.example.bake_boss_backend.service.SalesStockService;

@RestController
@RequestMapping("/api")
public class ProductController {
    private final MaterialsNameRepository materialsNameRepository;
    private final ItemMakeRepository itemMakeRepository;
    private final SupplierNameRepository supplierNameRepository;
    private final MaterialsRepository materialsRepository;
    private final CategoryNameRepository categoryNameRepository;
    private final ProductStockrepository productStockrepository;
    private final SalesStockRepository salesStockRepository;

    ProductController(MaterialsNameRepository materialsNameRepository, ItemMakeRepository itemMakeRepository,
            SupplierNameRepository supplierNameRepository, MaterialsRepository materialsRepository,
            CategoryNameRepository categoryNameRepository, ProductStockrepository productStockrepository, SalesStockRepository salesStockRepository) {
        this.materialsNameRepository = materialsNameRepository;
        this.itemMakeRepository = itemMakeRepository;
        this.supplierNameRepository = supplierNameRepository;
        this.materialsRepository = materialsRepository;
        this.categoryNameRepository = categoryNameRepository;
        this.productStockrepository = productStockrepository;
        this.salesStockRepository=salesStockRepository;
    }

    @Autowired
    private ProductStockService productStockService;

    @Autowired
    private SalesStockService salesStockService;

    @Autowired
    private ItemMakeService itemMakeService;

    @PostMapping("/addCategoryName")
    public ResponseEntity<?> addCategory(@RequestBody CategoryName categoryName) {
        if (categoryNameRepository.existsByUsernameAndCategoryName(categoryName.getUsername(),
                categoryName.getCategoryName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Category " + categoryName.getCategoryName() + " is already exists!");
        }
        CategoryName savedCategory = categoryNameRepository.save(categoryName);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
    }

    @PostMapping("/addMaterialsName")
    public ResponseEntity<?> addMaterials(@RequestBody MaterialName materialName) {
        if (materialsNameRepository.existsByUsernameAndMaterialsName(materialName.getUsername(),
                materialName.getMaterialsName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Materials " + materialName.getMaterialsName() + " is already exists!");
        }
        MaterialName savedMaterials = materialsNameRepository.save(materialName);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMaterials);
    }

    @PostMapping("/addSupplierName")
    public ResponseEntity<?> addSupplier(@RequestBody SupplierName supplierName) {
        if (supplierNameRepository.existsByUsernameAndSupplierName(supplierName.getUsername(),
                supplierName.getSupplierName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Materials " + supplierName.getSupplierName() + " is already exists!");
        }
        SupplierName savedSupplier = supplierNameRepository.save(supplierName);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSupplier);
    }

    @PostMapping("/itemMake")
    public ResponseEntity<?> newProducts(@RequestBody List<ItemMake> allItems) {
        for (ItemMake item : allItems) {
            List<ItemMake> existingItemByItemNo = itemMakeRepository.findByItemNo(item.getItemNo());
            List<ItemMake> existingItemByItemName = itemMakeRepository.findByItemName(item.getItemName());

            boolean itemExistsByNo = !existingItemByItemNo.isEmpty();
            boolean itemExistsByName = !existingItemByItemName.isEmpty();

            if (itemExistsByName && itemExistsByNo) {
                itemMakeRepository.save(item);
            } else if (itemExistsByName) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Sorry, this item already exists!");
            } else {
                itemMakeRepository.save(item);
            }
        }

        return ResponseEntity.ok(itemMakeRepository.findAll());
    }

    @PostMapping("/itemMakeNewAdd")
    public ResponseEntity<?> addProducts(@RequestBody List<ItemMake> allItems) {
        for (ItemMake item : allItems) {
            List<ItemMake> existingItemByItemNo = itemMakeRepository.findByItemNo(item.getItemNo());
            List<ItemMake> existingItemByItemName = itemMakeRepository.findByItemName(item.getItemName());
            List<ItemMake> existingItemByMaterialsName = itemMakeRepository
                    .findByItemNoAndMaterialsName(item.getItemNo(), item.getMaterialsName());

            boolean itemExistsByNo = !existingItemByItemNo.isEmpty();
            boolean itemExistsByName = !existingItemByItemName.isEmpty();
            boolean itemExistsByMaterialsName = !existingItemByMaterialsName.isEmpty();

            if (itemExistsByMaterialsName) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Sorry, this item already exists!");
            } else if (itemExistsByName && itemExistsByNo) {
                itemMakeRepository.save(item);
            }
        }
        return ResponseEntity.ok(itemMakeRepository.findAll());
    }

    @DeleteMapping("/deleteMaterial/{itemId}")
    public void getItem(@PathVariable Long itemId) {
        itemMakeRepository.deleteById(itemId);
    }

    @PutMapping("/updateItemMaterials/{itemId}")
    public ResponseEntity<ItemMake> updateItemMake(
            @PathVariable Long itemId,
            @RequestBody ItemMake itemMakeDetails) {

        return itemMakeRepository.findById(itemId).map(existingItem -> {
            existingItem.setMaterialsName(itemMakeDetails.getMaterialsName());
            existingItem.setQty(itemMakeDetails.getQty());
            ItemMake updatedItem = itemMakeRepository.save(existingItem);
            return ResponseEntity.ok(updatedItem);
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PostMapping("/addAllMaterials")
    public List<MaterialsStock> saveMaterials(@RequestBody List<MaterialsStock> allItems) {
        for (MaterialsStock newItem : allItems) {
            Optional<MaterialsStock> existingMaterialOpt = materialsRepository
                    .findLatestByMaterialsNameAndUsername(newItem.getMaterialsName(), newItem.getUsername());
            if (existingMaterialOpt.isPresent()) {
                MaterialsStock existingMaterial = existingMaterialOpt.get();
                Double newTotalQty = existingMaterial.getRemainingQty() + newItem.getMaterialsQty();
                Double newTotalValue = (existingMaterial.getRemainingQty() * existingMaterial.getAverageRate())
                        + (newItem.getMaterialsQty() * newItem.getMaterialsRate());
                Double newAverageRate = newTotalValue / newTotalQty;

                newItem.setAverageRate(newAverageRate);
                newItem.setRemainingQty(existingMaterial.getRemainingQty() + newItem.getMaterialsQty());
                materialsRepository.save(newItem);
            } else {
                newItem.setAverageRate(newItem.getMaterialsRate());
                newItem.setRemainingQty(newItem.getMaterialsQty());
                materialsRepository.save(newItem);
            }
        }

        return materialsRepository.findAll();
    }

    @PostMapping("/addAllProducts")
    public List<ProductStock> saveProducts(@RequestBody List<ProductStock> allItems) {
        for (ProductStock newItem : allItems) {
            Optional<ProductStock> latestProductStockOpt = productStockrepository
                    .findLatestProductStockByProductNameAndUsername(newItem.getProductName(), newItem.getUsername());

            if (latestProductStockOpt.isPresent()) {
                ProductStock latestProductStock = latestProductStockOpt.get();
                int newTotalQty = latestProductStock.getRemainingQty() + newItem.getProductQty();
                Double totalValue = (latestProductStock.getRemainingQty() * latestProductStock.getCostPrice()) +
                        (newItem.getProductQty() * newItem.getCostPrice());
                Double newCostPrice = totalValue / newTotalQty;
                newItem.setRemainingQty(latestProductStock.getRemainingQty() + newItem.getProductQty());
                newItem.setCostPrice(newCostPrice);

            } else {
                newItem.setRemainingQty(newItem.getProductQty());
                newItem.setCostPrice(newItem.getCostPrice());
            }
            productStockrepository.save(newItem);
        }
        return allItems;
    }

    @PostMapping("/addAdditionalSalesItemStock")
    public List<SalesStock> saveSalesStock(@RequestBody List<SalesStock> allItems) {
        for (SalesStock newItem : allItems) {
            Optional<SalesStock> latestProductStockOpt = salesStockRepository
                    .findLatestProductStockByProductNameAndUsername(newItem.getProductName(), newItem.getUsername());

            if (latestProductStockOpt.isPresent()) {
                SalesStock latestProductStock = latestProductStockOpt.get();
                int newTotalQty = latestProductStock.getRemainingQty() + newItem.getProductQty();
                Double totalValue = (latestProductStock.getRemainingQty() * latestProductStock.getCostPrice()) +
                        (newItem.getProductQty() * newItem.getCostPrice());
                Double newCostPrice = totalValue / newTotalQty;
                newItem.setRemainingQty(latestProductStock.getRemainingQty() + newItem.getProductQty());
                newItem.setCostPrice(newCostPrice);

            } else {
                newItem.setRemainingQty(newItem.getProductQty());
                newItem.setCostPrice(newItem.getCostPrice());
            }
            salesStockRepository.save(newItem);
        }
        return allItems;
    }
    @PostMapping("/productDistribution")
    public List<ProductStock> saveDistribution(@RequestBody List<ProductStock> allItems) {
        for (ProductStock newItem : allItems) {
            Optional<ProductStock> latestProductStockOpt = productStockrepository
                    .findLatestProductStockByProductNameAndUsername(newItem.getProductName(), newItem.getUsername());

            if (latestProductStockOpt.isPresent()) {
                ProductStock latestProductStock = latestProductStockOpt.get();
                int newTotalQty = latestProductStock.getRemainingQty() + newItem.getProductQty();
                Double totalValue = (latestProductStock.getRemainingQty() * latestProductStock.getCostPrice()) +
                        (newItem.getProductQty() * newItem.getCostPrice());
                Double newCostPrice = totalValue / newTotalQty;
                newItem.setRemainingQty(latestProductStock.getRemainingQty() - newItem.getProductQty());
                newItem.setCostPrice(newCostPrice);
            } else {
                newItem.setRemainingQty(newItem.getProductQty());
                newItem.setCostPrice(newItem.getCostPrice());
            }
            productStockrepository.save(newItem);
        }
        return allItems;
    }

    @PostMapping("/updateMaterialsStock")
    List<MaterialsStock> updateMaterials(@RequestBody List<MaterialsStock> allItems) {
        return materialsRepository.saveAll(allItems);
    }

    @GetMapping("/getCategoryName")
    public List<CategoryName> getCategoryNameByUsername(@RequestParam String username) {
        return categoryNameRepository.getCategoryNameByUsername(username);
    }

    @GetMapping("/getMaterialsName")
    public List<MaterialName> getMaterialsNameByUsername(@RequestParam String username) {
        return materialsNameRepository.getMaterialsNameByUsername(username);
    }

    @GetMapping("/getSuppliersName")
    public List<SupplierName> getSupplierNameByUsername(@RequestParam String username) {
        return supplierNameRepository.getSupplierNameByUsername(username);
    }

    @GetMapping("/getMadeProducts")
    public List<String> getMadeProductsByUsername() {
        return itemMakeRepository.findDistinctItems();
    }

    @GetMapping("/getItemList")
    public List<MadeItemDTO> getItemListByUsername(@RequestParam String username, String itemName) {
        return itemMakeRepository.findByUsernameGrouped(username, itemName);
    }

    @GetMapping("/getProductStock")
    public List<ProductStock> getLatestProductStockForEachProductName(String username) {
        return productStockrepository.findLatestProductStockForEachProductName(username);
    }

    @GetMapping("/getDamagedStock")
    public List<ProductStock> getDamagedStock(String username) {
        return productStockrepository.findDamagedProductByStatus(username);
    }

    @GetMapping("/getDamagedMaterial")
    public List<MaterialsStock> getDamagedMaterial(String username) {
        return materialsRepository.findDamagedMaterialsByStatus(username);
    }

    @GetMapping("/getMaterialsStock")
    public List<MaterialsStock> getMaterialsStock(String username) {
        return materialsRepository.findLatestMaterialsStockByUsername(username);
    }

    @GetMapping("/getSoldProduct")
    public List<ProductStock> getSoldProduct(String username) {
        return productStockService.getProductDistForCurrentMonth(username);
    }

    @GetMapping("/getAllProduct")
    public List<ProductStock> getAllProduct(String username) {
        return productStockService.getAllProductStock(username);
    }

    @GetMapping("/getAllMaterials")
    public List<MaterialsStock> getAllMaterials(String username) {
        return productStockService.getAllMaterialsStock(username);
    }

    @GetMapping("/getInvoiceData")
    public List<ProductStock> getInvoiceData(String username, String invoiceNo) {
        return productStockrepository.findByUsernameAndInvoiceNo(username, invoiceNo);
    }

    @GetMapping("/getSingleProduct")
    public Optional<ProductStock> getSingleProduct(@RequestParam Long productId) {
        return productStockrepository.findByProductId(productId);
    }

    @GetMapping("/getSingleMaterial")
    public Optional<MaterialsStock> getSingleMaterial(@RequestParam Long materialsId) {
        return materialsRepository.findByMaterialsId(materialsId);
    }

    @GetMapping("/pendingSalesStock")
    public List<ProductStock> getProductStockWithInvoiceNotInSalesStock(String customer) {
        return productStockService.getProductStockWithInvoiceNotInSalesStock(customer);
    }

    @PostMapping("/addSalesStock")
    public ResponseEntity<String> insertOrUpdateProductStockInSalesStock(
            @RequestBody Map<String, String> customerData) {
        String customer = customerData.get("customer");
        if (customer == null || customer.isEmpty()) {
            return ResponseEntity.badRequest().body("Customer is required");
        }
        salesStockService.insertOrUpdateProductStockInSalesStock(customer);
        return ResponseEntity.ok("Products added successfully");
    }

    @PutMapping("/productRateSetup")
    public ResponseEntity<ProductRate> productrate(@RequestBody ProductRate productRate) {
        ProductRate productRateSetup = productStockService.upsertProductRate(
                productRate.getUsername(),
                productRate.getProductName(),
                productRate.getSaleRate());
        return ResponseEntity.ok(productRateSetup);
    }

    @GetMapping("/getMaterials/grouped")
    public List<MaterialGroupedDto> getMaterialsGrouped(@RequestParam String username) {
        List<Object[]> results = itemMakeService.getMaterialsAndQtyGroupedByItemName(username);
        return results.stream()
                .map(result -> new MaterialGroupedDto((String) result[0], (String) result[1], (Double) result[2],
                        (Double) result[3]))
                .collect(Collectors.toList());
    }
}
