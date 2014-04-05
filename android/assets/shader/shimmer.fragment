#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif
varying LOWP vec4 v_color;
varying vec2 v_texCoords;
uniform float iGlobalTime;
uniform sampler2D u_texture;
void main()
{
  gl_FragColor = v_color * texture2D(u_texture, vec2(v_texCoords.x + sin(iGlobalTime * 3.0 + v_texCoords.y * 300.0) * 0.0005, v_texCoords.y)) * vec4(1.0,1.0,1.0,0.7 + 0.2 * sin(iGlobalTime * 1.5 + 50.0 + v_texCoords.y * 400.0));
}
