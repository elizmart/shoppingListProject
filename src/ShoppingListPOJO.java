public class ShoppingListPOJO {
    private Integer id;
    private String product;
    private Integer amount;
    private String status;
    private int parentListId;

    public ShoppingListPOJO(Integer id, String product, Integer amount, String status, int parentListId) {
        this.id = id;
        this.product = product;
        this.amount = amount;
        this.status = status;
        this.parentListId = parentListId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }


    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getParentListId() {
        return parentListId;
    }

    public void setParentListId(int parentListId) {
        this.parentListId = parentListId;
    }
}
