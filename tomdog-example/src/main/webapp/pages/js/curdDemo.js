var app = new Vue({
  el: "#app",
  data: {
    recipeIngridients : '',
    recipeName: '',
    editingRecipeIndex: null,
    itemKey: 'recipes',
    modalHeader: 'Add A Recipe',
    modalActionName: 'Add',
    lableActive : '',
    recipes : [
      {name: "Biryani", ingridients: ["Rice", "Curd", "Chicken", "Special pickle"]},
      {name: "Chicken Gravy", ingridients: ["Curd", "Chicken", "Onion", "Spices"]},
      {name: "Soybean Biryani", ingridients: ["Soybean", "Vegitables", "Spices"]}
    ]
  },
  mounted(){
    if (typeof(Storage) !== "undefined") {
        this.storageSupported = true;
        let items = localStorage.getItem(this.itemKey);
        if(items)
          this.recipes = JSON.parse(items);
    } else {
        this.storageSupported = false;
    }
    $(document).ready(function(){
      // the "href" attribute of the modal trigger must specify the modal ID that wants to be triggered
      $('.modal').modal({
          dismissible: false, // Modal can be dismissed by clicking outside of the modal
          opacity: .5
        }
      );
    });

  },
  methods:{
    addRecipe(e){
      e.preventDefault();
      if(this.editingRecipeIndex){
        this.recipes[this.editingRecipeIndex].name = this.recipeName;
        this.recipes[this.editingRecipeIndex].ingridients = this.recipeIngridients.split(",");
        Materialize.toast("Edited", 1000);
      }else {
        let ing = this.recipeIngridients.split(",")
        let recipe  = {
          name: this.recipeName,
          ingridients:  ing
        }
        this.recipes.push(recipe)
      }
      if(this.storageSupported){
        localStorage.setItem(this.itemKey, JSON.stringify(this.recipes));
      }
      this.editingRecipeIndex = null;
      $("#modal1").modal('close');
    },
    action(act, index){
      switch (act) {
        case 'add':
          $("#modal1").modal('open');
          break;
        case 'edit':
          this.editingRecipeIndex = index;
          this.modalHeader = "Edit Recipe";
          this.modalActionName = "Edit";
          this.recipeName = this.recipes[index].name;
          this.recipeIngridients = this.recipes[index].ingridients.join(",");
          this.lableActive = "active"
          $("#modal1").modal('open');

          break;
        case 'delete':
          this.recipes.splice(index, 1);
          this.editingRecipeIndex = null;
          localStorage.setItem(this.itemKey, JSON.stringify(this.recipes));
          break;
      }
    },
    modalClose(){
      this.editingRecipeIndex = null;
      this.recipeName = this.recipeIngridients = '';
      $("#modal1").modal('close');
    }
  }
});