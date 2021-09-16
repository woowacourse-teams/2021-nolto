import 'styled-components';

declare module 'styled-components' {
  export interface DefaultTheme {
    highLightedText: string;
    defaultText: string;
    background: string;
    headerStartColor: string;
    headerEndColor: string;
    headerShadow: string;
    moonRabbit: string;
    titleWeight: number;
  }
}
