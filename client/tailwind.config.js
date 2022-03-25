module.exports = {
  content: [
    "./src/**/*.{html,js}",
    "./public/index.html",
    "./public/**/*.{html,js}"
  ],
  theme: {
    colors:{
      'white':'#FFFFFF', 
      'purple':'#3F3CBB',
      'midnight':'#121063',
      'metal':'#565584',
      'tahiti-blue':'#3AB7BF',
      'cool-white':'#ECEBFF',
      'bubble-gum':'#FF77E9',
      'copper-rust':'#78DCCA',
    },
    fontFamily: {
      body: ['Inter', 'sans-serif'],
      header: ['Crimson Pro', 'serif']
    },
    container:{
      center:true
    },
    extend: {
      spacing:{
        '128':'40rem',
      }
    },
  },
  plugins: [],
}




// to start build process 
// npx tailwindcss -i ./src/input.css -o ./public/output.css --watch