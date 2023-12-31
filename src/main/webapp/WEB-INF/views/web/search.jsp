<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglib.jsp"%>
<script>document.title = 'Tìm kiếm | Book Buy'</script>
<div class="app__content">
    <div class="app-content__linker">
        <div class="grid wide">
            <ul class="app-content__linker-wrapper">
                <li><a href="<c:url value='/home' />"><span class="material-icons-outlined">home</span>Trang chủ</a></li>
                <li><a href="#">/ Tìm kiếm</a></li>
            </ul>
        </div>
    </div>
    <div class="app-content__search-text">
        <div class="grid wide">
            Bạn đang tìm kiếm theo danh mục: <a id="view-cate" href="#">Tất cả</a>
            Với nội dung tìm kiếm: <a id="view-search" href="#">Sách toán</a>
        </div>
    </div>
    <div class="app-content__books">
        <div class="grid wide">
            <div class="row">
                <div class="col l-3 m-4 c-12">
                    <div class="app-content-books__title"><span
                            class="material-icons-outlined">view_list</span>Danh mục sách</div>
                    <ul class="app-content-books__cate"></ul>
                </div>
                <div class="col l-9 m-8 c-12">
                    <div class="app-content-books__text">Kết quả tìm kiếm</div>

                    <!-- Render list book here -->
                    <div class="app-content-books__list"></div>

                    <!-- Pagination -->
                    <div class="app-content__pagination"></div>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="<c:url value='/template/js/main_base.js' />"></script>
<script src="<c:url value='/template/js/main_search.js' />"></script>