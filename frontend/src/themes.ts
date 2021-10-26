import { DefaultTheme } from 'styled-components';

import { PALETTE } from 'constants/palette';

export const defaultTheme: DefaultTheme = {
  highLightedText: PALETTE.WHITE_400,
  defaultText: PALETTE.BLACK_400,
  background: '#ffffff',
  headerStartColor: PALETTE.PRIMARY_200,
  headerEndColor: PALETTE.PRIMARY_400,
  headerShadow: 'drop-shadow(0 4px 4px rgba(0, 0, 0, 0.25))',
  pumpkin: 'hidden',
  titleWeight: 500,
};

export const halloweenTheme: DefaultTheme = {
  highLightedText: '#150A56',
  defaultText: PALETTE.WHITE_400,
  background: 'linear-gradient(#705087, #150A56)',
  headerStartColor: '#F4F6F0',
  headerEndColor: '#f6f6f6',
  headerShadow: 'drop-shadow(0 12px 12px #e9e9e9)',
  pumpkin: 'visible',
  titleWeight: 700,
};
