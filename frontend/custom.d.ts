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

declare module '*.gif';
