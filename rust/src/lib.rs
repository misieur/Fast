use jni::objects::{JByteArray, JClass, JString, JValue};
use jni::sys::{jbyteArray, jstring};
use jni::JNIEnv;
use once_cell::sync::Lazy;
use oxipng::{indexset, optimize_from_memory, Deflater, FilterStrategy, Options, StripChunks};
use std::io::Cursor;
use symphonia::core::io::MediaSourceStream;
use symphonia::core::meta::MetadataOptions;
use symphonia::core::probe::Hint;
use symphonia::default::get_probe;

static OPTIONS: Lazy<Options> = Lazy::new(|| Options {
    fix_errors: true,
    force: false,
    filters: indexset! {
        FilterStrategy::NONE,
        FilterStrategy::SUB,
        FilterStrategy::UP,
        FilterStrategy::AVERAGE,
        FilterStrategy::PAETH,
        FilterStrategy::MinSum,
        FilterStrategy::Entropy,
        FilterStrategy::Bigrams,
        FilterStrategy::BigEnt,
        FilterStrategy::Brute {
            num_lines: 8,
            level: 5,
        },
    },
    interlace: Some(false),
    optimize_alpha: true,
    bit_depth_reduction: true,
    color_type_reduction: true,
    palette_reduction: true,
    grayscale_reduction: true,
    idat_recoding: true,
    scale_16: false,
    strip: StripChunks::Safe,
    deflater: Deflater::Libdeflater { compression: 12 },
    fast_evaluation: false,
    timeout: None,
    max_decompressed_size: None,
});

#[unsafe(no_mangle)]
pub extern "system" fn Java_dev_misieur_fast_Native_getAudioProperties<'local>(
    mut env: JNIEnv<'local>,
    _class: JClass<'local>,
    input: JByteArray<'local>,
    ext: JString<'local>,
) -> jstring {
    let bytes: Vec<u8> = env
        .convert_byte_array(&input)
        .expect("Couldn't convert java byte array!");

    let cursor = Cursor::new(bytes);
    let mss = MediaSourceStream::new(Box::new(cursor), Default::default());
    let mut hint = Hint::new();
    if !ext.is_null() {
        hint.with_extension(env.get_string(&ext).expect("Couldn't get java string!").to_str().expect("Extension is not valid UTF-8"));
    }

    let probed = get_probe().format(&hint, mss, &Default::default(), &MetadataOptions::default());
    let format = probed.expect("Failed to read format!").format;

    if let Some(track) = format.default_track() {
        let params = &track.codec_params;

        let channels = params.channels.map(|c| c.count()).unwrap_or(0);
        let sample_rate = params.sample_rate.unwrap_or(0);
        let duration_seconds = if let Some(n_frames) = params.n_frames {
            n_frames as f64 / sample_rate as f64
        } else {
            0.0
        };

        let record_class = env
            .find_class("dev/misieur/fast/FastAudio$AudioProperties")
            .expect("Cannot find AudioProperties class");

        env.new_object(
            record_class,
            "(IIF)V",
            &[
                JValue::from(channels as i32),
                JValue::from(sample_rate as i32),
                JValue::from(duration_seconds as f32),
            ],
        )
        .expect("Failed to create AudioProperties object")
        .into_raw()
    } else {
        std::ptr::null_mut()
    }
}

#[unsafe(no_mangle)]
pub extern "system" fn Java_dev_misieur_fast_Native_optimizePng<'local>(
    env: JNIEnv<'local>,
    _class: JClass<'local>,
    input: JByteArray<'local>,
) -> jbyteArray {
    let input: Vec<u8> = env
        .convert_byte_array(input)
        .expect("Couldn't convert java byte array!");

    let output: JByteArray = env
        .byte_array_from_slice(
            &optimize_from_memory(&input, &OPTIONS).expect("Couldn't optimize image!"),
        )
        .expect("Couldn't create java byte array!");
    output.into_raw()
}
