import { DefaultTheme } from 'styled-components';

import { PALETTE } from 'constants/palette';

export const defaultTheme: DefaultTheme = {
  highLightedText: PALETTE.WHITE_400,
  defaultText: PALETTE.BLACK_400,
  background: '#fffff',
  headerStartColor: PALETTE.PRIMARY_200,
  headerEndColor: PALETTE.PRIMARY_400,
  headerShadow: 'drop-shadow(0 4px 4px rgba(0, 0, 0, 0.25))',
  moonRabbit: 'hidden',
  titleWeight: 500,
};

export const thanksgivingTheme: DefaultTheme = {
  highLightedText: '#0c1445',
  defaultText: PALETTE.WHITE_400,
  background: '#0c1445',
  headerStartColor: '#F4F6F0',
  headerEndColor: '#F4F1C9',
  headerShadow: 'drop-shadow(0 12px 12px #f4f1c9)',
  moonRabbit: 'visible',
  titleWeight: 700,
};
