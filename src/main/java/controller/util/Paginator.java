package controller.util;

public class Paginator {

    private final int limit;
    private final int offset;

    public Paginator(int page, int itemsPerPage){
        this.limit = itemsPerPage;
        this.offset = (page == 1) ? 0 : (page-1) * itemsPerPage+1;
    }


    public int getLimit(){return limit;}

    public int getOffset(){return offset;}
}
