/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        aegisBg: "#0A0E17",
        aegisCard: "#131B2E",
        aegisCardBorder: "#2B3A5A",
        aegisCyan: "#00F2FE",
        aegisViolet: "#7F00FF",
        aegisEmerald: "#00F5D4",
        aegisAmber: "#FFB800",
        aegisRed: "#FF2A6D",
        aegisDarkRed: "#3A0012",
      }
    },
  },
  plugins: [],
}
