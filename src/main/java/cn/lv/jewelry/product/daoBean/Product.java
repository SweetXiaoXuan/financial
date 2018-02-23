package cn.lv.jewelry.product.daoBean;

import cn.lv.jewelry.brand.daoBean.Brand;
import cn.lv.jewelry.designer.daoBean.Designer;

import javax.persistence.*;
import java.util.List;

@Table(name = "product")
@Entity
public class Product {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name;
    private int type;
	@Column(name = "cover_url")
	private String coverUrl;
	@ManyToOne
	@JoinColumn(name = "material")
	private ProductMaterial material;
	@ManyToOne
	@JoinColumn(name = "category")
	private ProductCategory category;
	@ManyToOne
	@JoinColumn(name = "brand")
	private Brand brand;
	@ManyToOne
	@JoinColumn(name = "origin")
	private ProductOrigin origin;
	@ManyToOne
	@JoinColumn(name = "embed")
	private ProductEmbed embed;
	@ManyToOne
	@JoinColumn(name = "style")
	private ProductStyle style;
    @ManyToMany
    @JoinTable(name = "PRODUCT_DESIGNER", joinColumns = { @JoinColumn(name="pid")}, inverseJoinColumns = { @JoinColumn(name = "did") })
    private List<Designer> designers;
    @ManyToMany
    @JoinTable(name = "PRODUCT_PRODUCT", joinColumns = { @JoinColumn(name="mid")}, inverseJoinColumns = { @JoinColumn(name = "rid") })
    private List<Product> products;
    @OneToMany(mappedBy = "pid")
    private List<ProductImage> images;
	@OneToMany(mappedBy = "pid")
	private List<ProductVideo> productVideos;
	private String length;
	private String size;
	private double price;
    @Column(name = "rent_count")
    private int rentCount;
    @Column(name = "buy_count")
    private int buyCount;
	@Column(name = "is_rent")
	private int isRent;
	private String description;
	private String postscript;
	private int status;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCoverUrl() {
		return coverUrl;
	}
	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}
	public ProductMaterial getMaterial() {
		return material;
	}
	public void setMaterial(ProductMaterial material) {
		this.material = material;
	}
	public ProductCategory getCategory() {
		return category;
	}
	public void setCategory(ProductCategory category) {
		this.category = category;
	}
	public Brand getBrand() {
		return brand;
	}
	public void setBrand(Brand brand) {
		this.brand = brand;
	}
	public ProductOrigin getOrigin() {
		return origin;
	}
	public void setOrigin(ProductOrigin origin) {
		this.origin = origin;
	}
	public ProductEmbed getEmbed() {
		return embed;
	}
	public void setEmbed(ProductEmbed embed) {
		this.embed = embed;
	}
	public ProductStyle getStyle() {
		return style;
	}
	public void setStyle(ProductStyle style) {
		this.style = style;
	}
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getIsRent() {
		return isRent;
	}
	public void setIsRent(int isRent) {
		this.isRent = isRent;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPostscript() {
		return postscript;
	}
	public void setPostscript(String postscript) {
		this.postscript = postscript;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getRentCount() {
        return rentCount;
    }

    public void setRentCount(int rentCount) {
        this.rentCount = rentCount;
    }

    public int getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(int buyCount) {
        this.buyCount = buyCount;
    }

    public List<Designer> getDesigners() {
        return designers;
    }

    public void setDesigners(List<Designer> designers) {
        this.designers = designers;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<ProductImage> getImages() {
        return images;
    }

    public void setImages(List<ProductImage> images) {
        this.images = images;
    }

	public List<ProductVideo> getProductVideos() {
		return productVideos;
	}

	public void setProductVideos(List<ProductVideo> productVideos) {
		this.productVideos = productVideos;
	}
}
