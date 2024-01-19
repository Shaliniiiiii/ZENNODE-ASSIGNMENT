const readline = require('readline');

const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});


let prices = [20, 40, 50];
let products = ["Product A", "Product B", "Product C"];
let quantities = new Array(3);
let giftWrap = new Array(3);

// Apply discounts on products
function applyTotalDiscount(quantities) {
    let cartTotalValue = quantities.reduce((sum, quantity) => sum + quantity, 0);

    let flat_10_discountValue = flat10DiscountValue(cartTotalValue);
    let bulk_5_discountValue = bulk5DiscountValue(quantities);
    let bulk_10_discountValue = bulk10DiscountValue(cartTotalValue);
    let tiered_50_discountValue = tiered50DiscountValue(quantities);

    // Choosing the most beneficial discount
    let maxDiscountApplied = Math.max(flat_10_discountValue, bulk_5_discountValue, bulk_10_discountValue, tiered_50_discountValue);

    // Determine which discount is applied
    let appliedDiscounts = [];
    if (maxDiscountApplied === flat_10_discountValue) 
        appliedDiscounts.push("Flat $10 Discount");
    if (maxDiscountApplied === bulk_5_discountValue) 
        appliedDiscounts.push("Bulk 5% Discount");
    if (maxDiscountApplied === bulk_10_discountValue) 
        appliedDiscounts.push("Bulk 10% Discount");
    if (maxDiscountApplied === tiered_50_discountValue) 
        appliedDiscounts.push("Tiered 50% Discount");

    console.log(`Discount values: Flat $10 = ${flat_10_discountValue}\nBulk 5% = ${bulk_5_discountValue}\nBulk 10% = ${bulk_10_discountValue}\nTiered 50% = ${tiered_50_discountValue}`);
    console.log(`Choosing the most beneficial discount: ${appliedDiscounts.join(", ")}`);        

    return { maxDiscountApplied, appliedDiscounts };
}

// Function to calculate flat $10 discount
function flat10DiscountValue(cartTotalValue) {
    if(cartTotalValue > 200) 
        return 10;
    return 0;
}

// Function to calculate bulk 5% discount
function bulk5DiscountValue(quantities) {
    let discount = 0;

    for (let i = 0; i < 3; i++) 
        if (quantities[i] > 10) 
           discount += (0.05 * prices[i] * quantities[i]);

    return discount;
}

// Function to calculate bulk 10% discount
function bulk10DiscountValue(cartTotalValue) {
    if(cartTotalValue > 20 ) 
            return (0.10 * cartTotalValue) ;
    return 0;
}


// Function to calculate tiered 50% discount
function tiered50DiscountValue(quantities) {
    let discount = 0;
    for (let i = 0; i < 3; i++) {
        if (quantities[i] > 30 && quantities[i] > 15) 
            discount += 0.50 * prices[i] * (quantities[i] - 15);
    }
    return discount;
}

// Function to calculate gift wrap fee
function calculateGiftWrapFee(quantities, giftWrap) {
    let giftWrapFee = 0;

    for (let i = 0; i < 3; i++) {
        if (giftWrap[i]) 
            giftWrapFee += quantities[i];
    }

    return giftWrapFee * 1;
}

// Function to calculate shipping fee
function calculateShippingFee(quantities) {
    let totalFee = 0;
        for (let i=0 ; i< quantities.length ;i++) {
            totalFee += quantities[i];
        }
    let packages = Math.ceil(totalFee / 10);
    return packages * 5;
}

// Function to get the applied discount
function getDiscountApplied(quantities) {
    let { maxDiscountApplied, appliedDiscounts } = applyTotalDiscount(quantities);

    if (appliedDiscounts.length > 1) {
        return `Multiple discounts applied on the purchase : ${appliedDiscounts.join(', ')}`;
    } 
    else if (appliedDiscounts.length === 1) {
        return appliedDiscounts[0];
    } 
    else {
        return "No Discount Applied";
    }
}

// Get quantity and gift wrap information for each product
function getInformation(index) {
    if (index < 3) {
        rl.question(`Enter ${products[index]} quantity: `, (quantity) => {
            quantities[index] = parseInt(quantity);
            rl.question(`Want to wrap ${products[index]} as a gift? (Type 1 for yes, 0 for no): `, (answer) => {
                giftWrap[index] = parseInt(answer) === 1;
                getInformation(index + 1);
            });
        });
    } else {
        rl.close();
        processAndDisplayInformation();
    }
}

function processAndDisplayInformation() {
    let subtotal = 0;
    for (let i = 0; i < 3; i++) {
        subtotal += prices[i] * quantities[i];
    }
    let discountAmount = applyTotalDiscount(quantities).maxDiscountApplied;
    let total = subtotal - discountAmount;

    console.log("\nProduct Details are:");

    for (let i = 0; i < 3; i++) {
        console.log(`${products[i]} = Quantity: ${quantities[i]} - Total: $${prices[i] * quantities[i]}`);
    }

    console.log(`\nSubtotal: $${subtotal}`);
    console.log(`Discount Applied: ${getDiscountApplied(quantities)}`);
    console.log(`Discount Amount: $${discountAmount}`);

    let giftWrapFee = calculateGiftWrapFee(quantities, giftWrap);
    let shippingFee = calculateShippingFee(quantities);

    console.log(`Total Gift Wrap Fee is: $${giftWrapFee}`);
    console.log(`Total Shipping Fee is: $${shippingFee}`);

    console.log(`\nTotal: $${total + giftWrapFee + shippingFee}`);
}


getInformation(0);
    