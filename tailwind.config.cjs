/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ['./src/main/jte/**/*.jte'],
  theme: {
    extend: {
      fontFamily: {
        sans: ['Plus Jakarta Sans', 'system-ui', 'sans-serif'],
        display: ['Outfit', 'Plus Jakarta Sans', 'sans-serif']
      }
    }
  },
  plugins: []
};
