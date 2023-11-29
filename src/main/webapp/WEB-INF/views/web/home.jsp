<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglib.jsp"%>
<script>document.title = 'Mua sách online nhanh nhất | Book Buy'</script>
<div class="app__content">
    <div class="app-content__linker">
        <div class="grid wide">
            <ul class="app-content__linker-wrapper">
                <li><a href="<c:url value='/home' />"><span class="material-icons-outlined">home</span>Trang chủ</a></li>
            </ul>
        </div>
    </div>
    <div class="grid wide">
        <div class="app-content__banner">
            <div class="row" style="height: 100%;">
                <div class="col l-10 m-12 c-12">
                    <div class="app-content-banner__slider">
                        <div class="app-content-banner-slider__img"></div>
                        <ul class="app-content-banner-slider__control">
                            <li></li>
                            <li></li>
                            <li></li>
                            <li></li>
                        </ul>
                    </div>
                </div>
                <div class="col l-2 m-0 c-0">
                    <div class="app-content-banner__featured-books">
                        <img src="<c:url value='/template/images/sai-gon-nhung-manh-ghep-roi-ky-uc_116334_1.png' />"
                            alt="featured-books">
                        <div class="app-content-banner__layer"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="app-content__books">
        <div class="grid wide">
            <div class="row">
                <div class="col l-3 m-4 c-12">
                    <div class="app-content-books__title"><span
                            class="material-icons-outlined">view_list</span>DANH MỤC SÁCH</div>
                    <ul class="app-content-books__cate"></ul>
                </div>
                <div class="col l-9 m-8 c-12">
                    <div class="app-content-books__text">SÁCH BÁN CHẠY</div>
                    <div class="app-content-books__list" id="ds-sach-ban-chay"></div>
                    
                    <div class="app-content-books__text" style="margin-top: 50px;">SÁCH MỚI</div>
                    <div class="app-content__pagination" id="page-sach-moi"></div>
                    <div class="app-content-books__list" id="ds-sach-moi"></div>
                    
                </div>
            </div>
        </div>
    </div>
</div>
<script src="<c:url value='/template/js/main_base.js' />"></script>
<script src="<c:url value='/template/js/main_home.js' />"></script>