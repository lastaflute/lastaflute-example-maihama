import './product-list';
import './product-detail';

route('/product/list/back', () => {
  riot.mount('content', 'product-list', { back: true });
});

route('/product/list..', () => {
  riot.mount('content', 'product-list', route.query());
});

route('/product/detail/*', (productId) => {
  riot.mount('content', 'product-detail', { productId });
});

riot.mixin('product', {
  api: {
    product: {
      status: '/product/list/status/',
      list: '/product/list/search/',
      detail: '/product/detail/'
    }
  }
});