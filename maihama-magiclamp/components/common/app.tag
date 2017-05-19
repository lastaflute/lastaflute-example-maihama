<app>
  <resource></resource>
  <helper></helper>
  <title-reactive></title-reactive>
  <div class="sg-container">
    <common-header></common-header>
    <content></content>
    <common-footer></common-footer>
  </div>

  <script>
    window.observable = riot.observable();

    route.base('/')

    route('', () => {
      riot.mount('content', 'root')
    })
    route('/product/list..', () => {
      riot.mount('content', 'product-list', route.query())
    })

    route('/product/detail/*', (productId) => {
      riot.mount('content', 'product-detail', {productId})
    })

    route.start(true)
  </script>
</app>
