interface SVGProps {
  fill?: string;
  width?: string;
  height?: string;
}

declare module '*.svg' {
  const content: ({ fill, width, height }: SVGProps) => JSX.Element;

  export default content;
}

declare module '*.jpeg';

declare module '*.png';

declare module '*.webp';

declare module '*.gif';

interface Window {
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  Kakao: any;
}
